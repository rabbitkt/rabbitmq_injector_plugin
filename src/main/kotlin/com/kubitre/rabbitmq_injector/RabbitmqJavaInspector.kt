package com.kubitre.rabbitmq_injector

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.psi.PsiMethod

class RabbitmqJavaInspector: AbstractBaseJavaLocalInspectionTool() {
    override fun checkMethod(method: PsiMethod, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        return ProblemDescriptor.EMPTY_ARRAY
    }
}