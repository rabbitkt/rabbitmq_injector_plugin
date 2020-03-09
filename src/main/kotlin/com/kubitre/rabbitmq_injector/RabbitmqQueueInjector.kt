package com.kubitre.rabbitmq_injector

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.LocalQuickFixProvider
import com.intellij.find.actions.ShowUsagesAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.injection.ReferenceInjector
import com.intellij.psi.meta.PsiMetaData
import com.intellij.psi.meta.PsiMetaOwner
import com.intellij.psi.meta.PsiPresentableMetaData
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.GlobalSearchScope.projectScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.intellij.util.ArrayUtil
import com.intellij.util.ProcessingContext
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.getParentOfType
import org.jetbrains.uast.toUElement
import javax.swing.Icon

class RabbitmqQueueInjector : ReferenceInjector() {
    override fun getId() = "rabbitmq-queue-name"

    override fun getDisplayName() = "Rabbitmq Queue Name"

    override fun getReferences(element: PsiElement, context: ProcessingContext, range: TextRange): Array<PsiReference> {
        return arrayOf(RabbitmqQueueReference(element))
    }

    class RabbitmqQueueReference(element: PsiElement):
            PsiReferenceBase<PsiElement>(element, ElementManipulators.getValueTextRange(element)),
            EmptyResolveMessageProvider, LocalQuickFixProvider {
        private fun getReferenceTypeName() = "Rabbitmq Queue Name"

        private fun getReferenceVariants(): Collection<String> {
            val uAnnotation = element.toUElement()?.getParentOfType<UAnnotation>() ?: return emptyList()
            val psiAnnotation = uAnnotation.javaPsi ?: return emptyList()
            throw Exception(psiAnnotation.toString())
            val annotationClass = psiAnnotation.nameReferenceElement?.resolve() as? PsiClass ?: return emptyList()
            println(annotationClass)
            val annotationFqn = annotationClass.qualifiedName ?: return emptyList()
            println(annotationFqn)

            val queues = mutableListOf<String>()
            for (member in AnnotatedElementsSearch.searchPsiMembers(annotationClass, projectScope(element.project))) {
                val attributeValue = member.getAnnotation(annotationFqn)?.findAttributeValue("queue_name")
            }
            return queues
        }

        override fun resolve(): PsiElement? {
            val value = value
            if (!validate(value)) return null

            return RabbitmqQueuePsiElement(element, value, getReferenceTypeName())
        }

        override fun getUnresolvedMessagePattern() = "Incorrect ${getReferenceTypeName()} ''{0}''"

        override fun getQuickFixes(): Array<LocalQuickFix>? = emptyArray()

        private fun validate(value: String) = value.isNotBlank() && !value.contains(Regex("\\s"))

        override fun getVariants() = ArrayUtil.toObjectArray(getReferenceVariants().map {
                LookupElementBuilder.create(it)
        })
    }

    class RabbitmqQueuePsiElement(
            private val parent: PsiElement,
            val queueName: String,
            private val typeName: String
    ): FakePsiElement(), PsiMetaOwner, PsiPresentableMetaData {
        override fun getName(context: PsiElement?) = ElementManipulators.getValueText(parent)
        override fun getParent() = parent
        override fun getIcon(): Icon? = null
        override fun getDeclaration() = this
        override fun getMetaData() = this
        override fun getTypeName() = typeName
        override fun init(element: PsiElement?) {}
        override fun getResolveScope() = GlobalSearchScope.allScope(project)
        override fun getUseScope() = GlobalSearchScope.allScope(project)
        override fun getNavigationElement() = this
        override fun isEquivalentTo(another: PsiElement?) = equals(another) || another != null && another is RabbitmqQueuePsiElement &&
                another.queueName == queueName

        override fun navigate(requestFocus: Boolean) {
            if (DumbService.getInstance(project).isDumb) return
            val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
            val popupPosition = JBPopupFactory.getInstance().guessBestPopupLocation(editor)

            ShowUsagesAction().startFindUsages(
                    this, popupPosition, editor, ShowUsagesAction.getUsagesPageSize()
            )
        }
    }
}