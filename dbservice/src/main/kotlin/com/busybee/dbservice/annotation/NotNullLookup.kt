package com.busybee.dbservice.annotation


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD) //on class level
annotation class NotNullLookup(val displayName: String = "")
