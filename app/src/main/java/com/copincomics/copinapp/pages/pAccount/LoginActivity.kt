package com.copincomics.copinapp.pages.pAccount

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.copincomics.copinapp.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.oAuthProvider
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_loigin.*
import kotlinx.android.synthetic.main.merge_top_title_with_finishbtn.*
import java.util.regex.Pattern

class LoginActivity : BaseAccount(), View.OnClickListener {

    lateinit var field_email: EditText
    lateinit var field_password: EditText
    lateinit var chkAutologin: CheckBox


    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    // Google
    val RC_SIGN_IN = 9001
    private lateinit var googleSignInClient: GoogleSignInClient

    // Facebook
    private lateinit var callbackManager: CallbackManager
    lateinit var facebook_btn_: LoginButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loigin)
        init()
        TAG = "BBB Login : "

        findViewById<TextView>(R.id.title).text = "Login"
        btn_finish.setOnClickListener { finish() }

        // 버튼
        val snsBtns = findViewById<View>(R.id.login_loginbtns)
        val googleBtn = snsBtns.findViewById<ImageView>(R.id.google_btn)
        val facebookBtn = snsBtns.findViewById<ImageView>(R.id.facebook_btn)
        facebook_btn_ = snsBtns.findViewById<LoginButton>(R.id.facebook_btn_)
        val twitterBtn = snsBtns.findViewById<ImageView>(R.id.twitter_btn)
        val appleBtn = snsBtns.findViewById<ImageView>(R.id.apple_btn)
        val emailBtn = findViewById<ImageView>(R.id.btn_login)


        setProgressBar(progress_bar)
        hideProgressBar()

        emailBtn.setOnClickListener(this)
        googleBtn.setOnClickListener(this)
        facebookBtn.setOnClickListener(this)
        twitterBtn.setOnClickListener(this)
        appleBtn.setOnClickListener(this)

        //
        field_password = findViewById(R.id.field_password)
        field_email = findViewById(R.id.field_email)
        chkAutologin = findViewById(R.id.login_autologin_chk)


        // firebase 초기화
        auth = Firebase.auth


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // facebook
        callbackManager = CallbackManager.Factory.create()
        facebook_btn_.setReadPermissions("email", "public_profile")
        facebook_btn_.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                signInWithCredential(credential)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                Toast.makeText(applicationContext, "Authentication Failed.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                Toast.makeText(
                    applicationContext,
                    "Authentication Failed. ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


        // 비번 찾기 액티비티 연결
        findViewById<TextView>(R.id.login_findpassword_btn).setOnClickListener {
            val intent = Intent(this, FindpwActivity::class.java)
            val bundle = Bundle()
            bundle.putString("id", "1234")
            intent.putExtras(bundle)
            this.startActivity(intent)
        }

        // 회원가입 액티비티 연결
        findViewById<TextView>(R.id.login_signup_btn).setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            val bundle = Bundle()
            bundle.putString("id", "1234")
            intent.putExtras(bundle)
            this.startActivity(intent)
        }


    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> signInWithEmail(field_email.text.toString(), field_password.text.toString())
            R.id.google_btn -> googleSignIn()
            R.id.facebook_btn -> facebook_btn_.performClick()
            R.id.twitter_btn -> startActivityForSignInWithProvider("twitter.com")
            R.id.apple_btn -> startActivityForSignInWithProvider("apple.com")
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

                signInWithCredential(credential)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    // google & facebook
    fun signInWithCredential(credential: AuthCredential) {
        showProgressBar()

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    var user = auth.currentUser
                    Toast.makeText(this, "Sign in Success", Toast.LENGTH_LONG).show()

                    user = FirebaseAuth.getInstance().currentUser!!
                    updateUserInfo(user, type = "login")

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    // [START_EXCLUDE]
                    // [END_EXCLUDE]
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }

                // [START_EXCLUDE]
//                hideProgressBar()
                // [END_EXCLUDE]
            }

    }


    private fun validateForm(): Boolean {
        var valid = true

        val email = field_email.text.toString()
        if (TextUtils.isEmpty(email)) {
            field_email.error = "Required."
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            field_email.error = "Email Format Required."
            valid = false
        } else {
            field_email.error = null
        }

        val password = field_password.text.toString()
        if (TextUtils.isEmpty(password)) {
            field_password.error = "Required."
            valid = false
        } else if (password.length < 6) {
            field_password.error = "Please enter at least 6 characters for the password."
            valid = false

        } else if (!Pattern.matches("^.{6,20}$", password)) {
            field_password.error = "Invalid password."
            valid = false

        } else {
            field_password.error = null
        }

        return valid
    }

    // Google
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    // Twitter & apple
    private fun startActivityForSignInWithProvider(providerId: String = "twitter.com") {

        val customScopes = ArrayList<String>()

        auth.startActivityForSignInWithProvider(this,
            oAuthProvider(providerId, auth) {
                scopes = customScopes
            })
            .addOnSuccessListener { authResult ->
                Log.d(TAG, "activitySignIn:onSuccess:${authResult.user}")
                Toast.makeText(
                    baseContext, "Authentication success.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUserInfo(authResult.user!!)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "activitySignIn:onFailure", e)
                Toast.makeText(
                    baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()

            }

    }

    // email signin
    private fun signInWithEmail(email: String, password: String) {
        if (!validateForm()) {
            return
        }

        showProgressBar()
        hideKeyboard(this.currentFocus!!)

        // [START create_user_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "SignInUserWithEmail:success ${auth.currentUser}")
                    val user = auth.currentUser
                    updateUserInfo(user!!)
                    hideProgressBar()
                    // btnClose.performClick()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    hideProgressBar()
                }
            }
    }

}