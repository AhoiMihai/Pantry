package com.ahoi.pantry.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ahoi.pantry.TestSchedulerProvider
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.profile.api.ProfileRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyString


class AuthenticationViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val userId = "userid"
    private val authManagerMock: AuthManager = mock {
        on {
            it.loginWithEmailAndPassword(
                anyString(),
                anyString()
            )
        } doReturn Single.just(userId)
        on {
            it.signUpWithEmailAndPassword(anyString(), anyString())
        } doReturn Single.just(
            userId
        )
        on { it.currentUserId } doReturn userId
    }

    private val profileRepoMock: ProfileRepository = mock {
        on {
            it.createProfile(
                anyString(),
                anyString(),
                anyString()
            )
        } doReturn Completable.complete()
        on { it.loadProfile(anyString()) } doReturn Completable.complete()
    }

    private val scheduler = TestSchedulerProvider(Schedulers.trampoline())

    private val sut = AuthenticationViewModel(
        authManagerMock,
        scheduler,
        profileRepoMock
    )

    @Test
    fun testUpdatePassword() {
        sut.updatePassword("Password")

        assertEquals("Password", sut.password.value)
    }

    @Test
    fun testUpdateEmail() {
        sut.updateEmail("email")

        assertEquals("email", sut.email.value)
    }

    @Test
    fun testUpdateUsername() {
        sut.updateUserName("username")

        assertEquals("username", sut.username.value)
    }

    @Test
    fun testLoginCallsCorrectFunction() {
        sut.updatePassword("password")
        sut.updateEmail("email")

        sut.authenticate(AuthMode.LOGIN)

        verify(authManagerMock).loginWithEmailAndPassword("email", "password")
    }

    @Test
    fun testSignUpCallsCorrectFunction() {
        sut.updatePassword("password")
        sut.updateUserName("username")
        sut.updateEmail("email")

        sut.authenticate(AuthMode.SIGN_UP)

        verify(authManagerMock).signUpWithEmailAndPassword("email", "password")
    }

    @Test
    fun testLoginLoadsProfile() {
        sut.updatePassword("password")
        sut.updateEmail("email")

        sut.authenticate(AuthMode.LOGIN)

        verify(profileRepoMock).loadProfile(userId)
    }

    @Test
    fun testSignUpCreatesProfile() {
        sut.updatePassword("password")
        sut.updateUserName("username")
        sut.updateEmail("email")

        sut.authenticate(AuthMode.SIGN_UP)

        verify(profileRepoMock).createProfile(userId, "username", "email")
    }

    @Test
    fun testSignUpSuccess() {
        sut.updatePassword("password")
        sut.updateUserName("username")
        sut.updateEmail("email")

        sut.authenticate(AuthMode.SIGN_UP)

        assertEquals(sut.operationResult.value, CommonOperationState.SUCCESS)
    }

    @Test
    fun testLoginSuccess() {
        sut.updatePassword("password")
        sut.updateEmail("email")

        sut.authenticate(AuthMode.LOGIN)

        verify(authManagerMock).loginWithEmailAndPassword("email", "password")
        verify(profileRepoMock).loadProfile(userId)
        assertEquals(sut.operationResult.value, CommonOperationState.SUCCESS)
    }

    @Test
    fun testLoginNoPassword() {
        sut.updateEmail("email")

        sut.authenticate(AuthMode.LOGIN)

        assertEquals(sut.operationResult.value, OperationResult.MISSING_CREDENTIALS)
    }

    @Test
    fun testLoginNoEmail() {
        sut.updatePassword("password")

        sut.authenticate(AuthMode.LOGIN)

        assertEquals(sut.operationResult.value, OperationResult.MISSING_CREDENTIALS)
    }

    @Test
    fun tesSignUpNoPassword() {
        sut.updateEmail("email")

        sut.authenticate(AuthMode.SIGN_UP)

        assertEquals(sut.operationResult.value, OperationResult.MISSING_CREDENTIALS)
    }

    @Test
    fun testSignUoNoEmail() {
        sut.updatePassword("password")

        sut.authenticate(AuthMode.SIGN_UP)

        assertEquals(sut.operationResult.value, OperationResult.MISSING_CREDENTIALS)
    }

    @Test
    fun testSignUpFail() {
        whenever(
            authManagerMock.signUpWithEmailAndPassword(
                anyString(),
                anyString()
            )
        ) doReturn Single.error(IllegalArgumentException())
        sut.updatePassword("password")
        sut.updateUserName("username")
        sut.updateEmail("email")

        sut.authenticate(AuthMode.SIGN_UP)

        assertEquals(sut.operationResult.value, CommonOperationState.UNKNOWN_ERROR)
    }

    @Test
    fun testLoginFail() {
        whenever(
            authManagerMock.loginWithEmailAndPassword(
                anyString(),
                anyString()
            )
        ) doReturn Single.error(IllegalArgumentException())
        sut.updatePassword("password")
        sut.updateEmail("email")

        sut.authenticate(AuthMode.LOGIN)

        assertEquals(sut.operationResult.value, CommonOperationState.UNKNOWN_ERROR)
    }
}