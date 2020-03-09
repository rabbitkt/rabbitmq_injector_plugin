package com.kubitre.rabbitmq_injector

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import javax.swing.JComponent

class Dialog() : DialogWrapper(true) {
    init {
        title = "моё первое окно в идее"
        window.setSize(150, 500)
    }
    override fun createCenterPanel(): JComponent? {
        val panel = panel  {
            row {
                label("test")
                label("test")
                label("test")
                label("test")

            }
            row {
                cell {
                    label("test")
                    label("test")
                    label("test")
                    label("test")
                    label("test")
                    label("test")

                }
            }
        }
        return panel
    }
}