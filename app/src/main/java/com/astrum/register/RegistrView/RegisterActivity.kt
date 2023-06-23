package com.astrum.register.RegistrView

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.astrum.register.R
import com.astrum.register.RegisterDate.ValidateEmailBodi
import com.astrum.register.RegistrRepository.AuthRepository
import com.astrum.register.RegistrUtils.APIService
import com.astrum.register.RegistrViewModel.RegisterActivityViewModel
import com.astrum.register.RegistrViewModel.RegisterActivityViewModelFactori
import com.astrum.register.databinding.ActivityRegisterBinding
import java.lang.StringBuilder

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mViewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.name.onFocusChangeListener = this
        mBinding.email.onFocusChangeListener = this
        mBinding.password.onFocusChangeListener = this
        mBinding.confirmPassword.onFocusChangeListener = this
        mViewModel = ViewModelProvider(
            this,
            RegisterActivityViewModelFactori(AuthRepository(APIService.getService()), application)
        ).get(RegisterActivityViewModel::class.java)
        setupObservers()

    }

    private fun setupObservers() {
        mViewModel.getIsLoading().observe(this) {
            mBinding.progressBar.isVisible = it
        }

        mViewModel.getIsUniqueEmail().observe(this){
           if (validateEmail(shouldUpdateView = false)){
               if (it){
                   mBinding.layoutEmail.apply {
                       if (isErrorEnabled) isErrorEnabled = false
                       setStartIconDrawable(R.drawable.check_ic)
                       setStartIconTintList(ColorStateList.valueOf(Color.WHITE))
                   }
               }else{
                   mBinding.layoutEmail.apply {
                       if (startIconDrawable != null) startIconDrawable = null
                       isErrorEnabled = true
                       error = "Email is already taken"
                   }
               }
           }
        }

        mViewModel.getErrorMessage().observe(this) {
            //fulName Email Password
            val formatErrorKey = arrayOf("fuulName", "email", "password")
            val message = StringBuilder()
            it.map { entry ->
                if (formatErrorKey.contains(entry.key)) {
                    when (entry.key) {
                        "fullName" -> {
                            mBinding.layoutName.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "email" -> {
                            mBinding.layoutEmail.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "password" -> {
                            mBinding.layoutPassword.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                    }
                } else {
                    message.append(entry.value).append("\n")

                }
                if (message.isNotEmpty())
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_info)
                        .setTitle("INFORMATION")
                        .setMessage(message)
                        .setPositiveButton("Ok") { dialog, _ -> dialog!!.dismiss() }
                        .show()

            }
            mViewModel.getUser().observe(this) {

            }
        }
    }

        private fun validateFullName(): Boolean {
            var errorMessage: String? = null
            val value: String = mBinding.name.text.toString()
            if (value.isEmpty()) {
                errorMessage = "Full name is required"
            }

            if (errorMessage != null) {
                mBinding.layoutName.apply {
                    isErrorEnabled = true
                    error = errorMessage
                }
            }
            return errorMessage == null

        }

        private fun validateEmail(shouldUpdateView:Boolean = true): Boolean {
            var errorMessage: String? = null
            val value: String = mBinding.email.text.toString()
            if (value.isEmpty()) {
                errorMessage = "Email name is required"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                errorMessage = "Email address is invalid"
            }
            if (errorMessage != null && shouldUpdateView) {
                mBinding.layoutEmail.apply {
                    isErrorEnabled = true
                    error = errorMessage
                }
            }
            return errorMessage == null

        }

        private fun validatePassword(): Boolean {
            var errorMessage: String? = null
            val value: String = mBinding.password.text.toString()
            if (value.isEmpty()) {
                errorMessage = "Password is required"
            } else if (value.length < 6) {
                errorMessage = "Password must be 6 characters long"
            }
            if (errorMessage != null) {
                mBinding.layoutPassword.apply {
                    isErrorEnabled = true
                    error = errorMessage
                }
            }
            return errorMessage == null

        }

        private fun validateConfirmPassword(): Boolean {
            var errorMessage: String? = null
            val value: String = mBinding.confirmPassword.text.toString()
            if (value.isEmpty()) {
                errorMessage = "Confirm Password is required"
            } else if (value.length < 6) {
                errorMessage = "Confirm Password must be 6 characters long"
            }
            if (errorMessage != null) {
                mBinding.layoutConfirmPassword.apply {
                    isErrorEnabled = true
                    error = errorMessage
                }
            }
            return errorMessage == null

        }

        private fun validatePasswordAndConfirmPassword(): Boolean {
            var errorMessage: String? = null
            val password = mBinding.password.text.toString()
            val confirmPassword = mBinding.confirmPassword.text.toString()
            if (password != confirmPassword) {
                errorMessage = "Confirm Password doesn`t match with password"
            }
            if (errorMessage != null) {
                mBinding.layoutConfirmPassword.apply {
                    isErrorEnabled = true
                    error = errorMessage
                }
            }
            return errorMessage == null

        }


        override fun onClick(view: View?) {
        }

        override fun onFocusChange(view: View?, hasFocus: Boolean) {
            if (view != null) {
                when (view.id) {
                    R.id.name -> {
                        if (hasFocus) {
                            if (mBinding.layoutName.isErrorEnabled) {
                                mBinding.layoutName.isErrorEnabled = false
                            }
                        } else {
                            validateFullName()
                        }
                    }

                    R.id.email -> {
                        if (hasFocus) {
                            if (mBinding.layoutEmail.isErrorEnabled) {
                                mBinding.layoutEmail.isErrorEnabled = false
                            }
                        } else {
                            if (validateEmail()) {
                                //do validation for its uniqueness
                                //uning o'ziga xosligini tekshirish
                                mViewModel.validateEmailAddress(ValidateEmailBodi(mBinding.email.text!!.toString()))
                            }
                        }
                    }

                    R.id.password -> {
                        if (hasFocus) {
                            if (mBinding.layoutPassword.isErrorEnabled) {
                                mBinding.layoutPassword.isErrorEnabled = false
                            }
                        } else {
                            if (validatePassword() && mBinding.confirmPassword.text!!.isNotEmpty() && validateConfirmPassword() &&
                                validatePasswordAndConfirmPassword()
                            ) {
                                if (mBinding.layoutConfirmPassword.isErrorEnabled) {
                                    mBinding.layoutConfirmPassword.isErrorEnabled = false
                                }
                                mBinding.layoutConfirmPassword.apply {
                                    setStartIconDrawable(R.drawable.check_ic)
                                    setStartIconTintList(ColorStateList.valueOf(Color.WHITE))
                                }
                            }
                        }
                    }

                    R.id.confirmPassword -> {
                        if (hasFocus) {
                            if (mBinding.layoutConfirmPassword.isErrorEnabled) {
                                mBinding.layoutConfirmPassword.isErrorEnabled = false
                            }
                        } else {
                            if (validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()) {
                                if (mBinding.layoutPassword.isErrorEnabled) {
                                    mBinding.layoutPassword.isErrorEnabled = false
                                }
                                mBinding.layoutConfirmPassword.apply {
                                    setStartIconDrawable(R.drawable.check_ic)
                                    setStartIconTintList(ColorStateList.valueOf(Color.WHITE))
                                }
                            }
                        }
                    }


                }
            }
        }


        override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
            return false
        }
    }


