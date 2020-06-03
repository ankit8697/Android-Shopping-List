package hu.ait.mycart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var anim = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.splash_animation)

        anim.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    val intent = Intent(this@SplashActivity, ScrollingActivity::class.java)
                    startActivity(intent)
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            }
        )
        ivCart.startAnimation(anim)


    }
}
