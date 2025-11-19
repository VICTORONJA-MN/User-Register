/**
 * DATA CLASS: User
 * MODELO DE DOMINIO - Representa la entidad usuario en el sistema
 *
 * @property id Identificador único auto-generado (valor por defecto: 0)
 * @property name Nombre completo del usuario (requerido)
 * @property email Correo electrónico único (requerido)
 * @property phone Número telefónico (requerido)
 *
 * CARACTERÍSTICAS TÉCNICAS:
 * - Data Class: Optimizada para almacenamiento y transporte de datos
 * - Parcelable implícito: Compatible con Intents y Bundles
 * - Valores por defecto: Facilita la creación de instancias
 * - Inmutabilidad: Campos declarados como 'val' para thread-safe
 */
package com.example.miappusuarios.data

//atributos de la clase
data class User(
    val id: Long = 0,                    // PRIMARY KEY - Auto increment
    val name: String,                    // NOT NULL constraint
    val email: String,                   // NOT NULL constraint
    val phone: String                    // NOT NULL constraint
)