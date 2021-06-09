package org.anitab.mentorship.utils

fun String.checkPasswordSecurity(): Boolean {
    var containsSmallLetter = false
    var containsCapitalLetter = false
    var containsNumber = false
    var containsSpecialSign = false

    forEach {
        when (it) {
            in 'a'..'z' -> containsSmallLetter = true
            in 'A'..'Z' -> containsCapitalLetter = true
            in '0'..'9' -> containsNumber = true
            in '!'..'/' -> containsSpecialSign = true
            in ':'..'@' -> containsSpecialSign = true
            in '['..'`' -> containsSpecialSign = true
            in '{'..'~' -> containsSpecialSign = true
        }
    }

    return (containsSmallLetter && containsCapitalLetter && containsNumber && containsSpecialSign)
}
