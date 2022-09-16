package com.example.users

import com.example.users.util.ValidationObject.validateEmail
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ValidationObjectTest {

    /*Set Up Mock Input Data*/
    private val emailTrue: String = "johndoe@gmail.com"
    private val emailFalse: String = "johndoe@com"

    /*Test Validate Email Function with Correct Mail*/
    @Test
    fun test_validateEmail_isTrue() {
        val result = validateEmail(emailTrue)
        assertTrue(result)
    }

    /*Test Validate Email Function with Wrong Email*/
    @Test
    fun test_validateEmail_isFalse() {
        val result = validateEmail(emailFalse)
        assertFalse(result)
    }
}
