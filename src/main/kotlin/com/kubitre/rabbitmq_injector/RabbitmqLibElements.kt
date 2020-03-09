package com.kubitre.rabbitmq_injector

const val rpc_rabbitmq_lib_package_name = "org.eltex.eccm.rpcrabbitmqlib"

const val rabbitmq_server_annotation = "$rpc_rabbitmq_lib_package_name.server.RPCServer"
const val rabbitmq_handler_annotation = "$rpc_rabbitmq_lib_package_name.mapper.RPCHandler"
const val rabbitmq_route_annotation = "org.eltex.eccm.rpcrabbitmqlib.mapper.RPCRoute"

const val rabbitmq_contract_id = "contractID"

val ANNOTATION_CLASS = listOf(rabbitmq_handler_annotation, rabbitmq_server_annotation)

