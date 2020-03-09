package com.kubitre.rabbitmq_injector

import com.intellij.compiler.CompilerConfiguration
import com.intellij.compiler.CompilerConfigurationImpl
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.DialogWrapperDialog
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.terminal.JBTerminalWidget
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.layout.panel
import com.intellij.ui.popup.PopupComponent
import com.intellij.ui.tabs.JBTabsFactory
import javax.swing.JButton
import javax.swing.event.HyperlinkEvent

class CheckAnnotationProcessorsStartupActivity: StartupActivity {
    override fun runActivity(project: Project) {
        DumbService.getInstance(project).runWhenSmart {
            val javaPsiFacade = JavaPsiFacade.getInstance(project)
            val projectScope = GlobalSearchScope.allScope(project)
            val statusBar = WindowManager.getInstance().getStatusBar(project)

            if (project.name == "eccm") {
                val compilerConfiguration = getCompilerConfiguration(project)
                if (!compilerConfiguration.defaultProcessorProfile.isEnabled) {
                    suggestEnableAnnotation(project)
                }
            }
        }
    }

    private fun getCompilerConfiguration(project: Project) = CompilerConfiguration.getInstance(project) as CompilerConfigurationImpl

    private fun suggestEnableAnnotation(project: Project) {
        val statusBar = WindowManager.getInstance().getStatusBar(project)

        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(
                        "Активировать annotation processors для библиотеки RpcRabbitmqLib? <a href=\"enable\">Активировать</a>",
                        MessageType.WARNING
                ) {
                    event ->
                    if (event.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                        enableAnnotations(project)
                    }
                }
                .setHideOnLinkClick(true)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.component), Balloon.Position.atRight)
    }

    private fun enableAnnotations(project: Project) {
        getCompilerConfiguration(project).defaultProcessorProfile.isEnabled = true

        val statusBar = WindowManager.getInstance().getStatusBar(project)
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(
                        "Annotation Processing был активирован",
                        MessageType.INFO,
                        null
                )
                .setFadeoutTime(3_000)
                .createBalloon()
                .show(RelativePoint.getNorthEastOf(statusBar.component), Balloon.Position.atRight)
    }
}