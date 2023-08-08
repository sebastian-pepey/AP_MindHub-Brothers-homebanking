package com.mindhub.homebanking.models;

// Las class "enum" es una clase especial que representa un grupo de constantes.

public enum TransactionType {
    CREDIT,
    DEBIT
}

// Para accederlo, se emplea la dot-notation, es decir: asignamos a una variable
// tipo TransactionType, TransactionType.CREDIT