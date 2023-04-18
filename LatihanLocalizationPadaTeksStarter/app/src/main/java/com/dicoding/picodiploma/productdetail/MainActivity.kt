package com.dicoding.picodiploma.productdetail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import com.dicoding.picodiploma.productdetail.databinding.ActivityMainBinding
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupData()
    }

    private fun setupData() {
        val data = RemoteDataSource(this)
        val product = data.getDetailProduct()

        product.apply {
            binding.apply {
                previewImageView.setImageResource(image)
                nameTextView.text = name
                storeTextView.text = store
                colorTextView.text = color
                sizeTextView.text = size
                descTextView.text = desc
                priceTextView.text = price.withCurrencyFormat()
                dateTextView.text = getString(R.string.dateFormat, date.withDateFormat())
                ratingTextView.text = getString(
                    R.string.ratingFormat,
                    rating.withNumberingFormat(),
                    countRating.withNumberingFormat()
                )
            }
        }

        setupAccessibility(product)
    }

    private fun setupAccessibility(productModel: ProductModel) {
        productModel.apply {
            binding.apply {
                settingImageView.contentDescription = getString(R.string.settingDescription)
                previewImageView.contentDescription = getString(R.string.previewDescription)
                colorTextView.contentDescription = getString(R.string.colorDescription, color)
                sizeTextView.contentDescription = getString(R.string.sizeDescription, size)
                ratingTextView.contentDescription = getString(
                    R.string.ratingDescription,
                    rating.withNumberingFormat(),
                    countRating.withNumberingFormat()
                )
                storeTextView.contentDescription = getString(R.string.storeDescription, store)
            }
        }
    }

    private fun setupAction() {
        binding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}

private fun String.withNumberingFormat(): String {
    return NumberFormat.getNumberInstance().format(this.toDouble())
}

fun String.withDateFormat(): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val date = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}

fun String.withCurrencyFormat(): String {
    val rupiahExchangeRate = 12495.95
    val euroExchangeRate = 0.88

    var priceOnDollar = this.toDouble() / rupiahExchangeRate

    var mCurrencyFormat = NumberFormat.getCurrencyInstance()
    val deviceLocale = Locale.getDefault().country
    when {
        deviceLocale.equals("ES") -> {
            priceOnDollar *= euroExchangeRate
        }
        deviceLocale.equals("ID") -> {
            priceOnDollar *= rupiahExchangeRate
        }
        else -> {
            mCurrencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        }
    }
    return mCurrencyFormat.format(priceOnDollar)
}
