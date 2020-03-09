package com.kubitre.rabbitmq_injector

import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import org.jetbrains.uast.UMethod

class RabbitmqRPCRouteInspection: AbstractBaseUastLocalInspectionTool() {
    override fun checkMethod(method: UMethod, manager: InspectionManager, isOnTheFly: Boolean):
            Array<ProblemDescriptor>? {
        val annotation = method.getAnnotations()
        method.findAnnotation(rabbitmq_route_annotation)?.let {
//            val containingFile = method.containingFile
//            val problemHolder = ProblemsHolder(manager, containingFile, isOnTheFly)

            val value = it.findAttributeValue(rabbitmq_contract_id)
            throw Exception(value?.lang?.displayName)
        }
        return ProblemDescriptor.EMPTY_ARRAY
    }
}