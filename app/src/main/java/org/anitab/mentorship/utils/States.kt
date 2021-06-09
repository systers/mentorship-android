package org.anitab.mentorship.utils

/**
 * A sealed class to represent error states
 * in the EditProfileFragment. More error states
 * can be added in the future as the application
 * grows in complexity.
 */
sealed class EditProfileFragmentErrorStates {

    /**
     * An object to represent the error case when the
     * entered name of the user is empty.
     */
    object EmptyNameError : EditProfileFragmentErrorStates()

    /**
     * An object to represent the error case when the
     * entered name of the user is too short.
     */
    object NameTooShortError : EditProfileFragmentErrorStates()

    /**
     * An object to represent the error case when the
     * entered name of the user is too long.
     */
    object NameTooLongError : EditProfileFragmentErrorStates()
}
