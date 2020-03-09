package com.kubitre.rabbitmq_injector

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.*
import java.lang.Exception

class RabbitmqRPCUsageProvider: ImplicitUsageProvider {
    override fun isImplicitWrite(element: PsiElement): Boolean {
        TODO()
//        return element is PsiField
//                && AnnotationUtil.isAnnotated(element, )
    }

    override fun isImplicitRead(element: PsiElement): Boolean {
        return false
    }

    override fun isImplicitUsage(element: PsiElement): Boolean {
        if (element is PsiClass) {
            return (element.hasAnnotation(rabbitmq_server_annotation))
        }
        if (element is PsiMethod) {
            return element.hasModifierProperty(PsiModifier.PUBLIC)
                    && !element.hasAnnotation(rabbitmq_route_annotation)
                    && element.containingClass?.hasAnnotation(rabbitmq_handler_annotation) ?: false
        }
        return false
    }
}