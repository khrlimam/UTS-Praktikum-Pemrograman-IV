package school.app.utspraktikumpemrogramaniv

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.checkout_items.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class MainActivity : AppCompatActivity() {

    private var qty: Int = 0
    private var ramPrice = .0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomSheet = BottomSheetBehavior.from(checkoutContainer)
        checkoutContainer.setOnClickListener { bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }

        rgRam.setOnCheckedChangeListener { _, checkedId ->
            ramPrice = when (checkedId) {
                R.id.rb2 -> 300000.0
                R.id.rb4 -> 650000.0
                R.id.rb8 -> 900000.0
                else -> .0
            }
            updatePrice()
            tvRamC.text = findViewById<RadioButton>(checkedId).text.toString()
            tvPriceC.text = ramPrice.rp()
        }
        etQty.addTextChangedListener(textWatcher {
            qty = etQty.text.toString().defaultZero()
            updatePrice()
            tvQtyC.text = qty.toString()
        })
        etItemName.addTextChangedListener(textWatcher {
            tvItemNameC.text = etItemName.text.toString()
        })
    }

    private fun totalPrice() = qty * ramPrice

    private fun updatePrice() {
        tvTotal.text = totalPrice().rp()
    }

    private fun textWatcher(onChange: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onChange()
            }
        }
    }

}

fun Double.rp(): String {
    val idr = DecimalFormat.getCurrencyInstance() as DecimalFormat
    val formatRp = DecimalFormatSymbols()

    formatRp.setCurrencySymbol("Rp")
    formatRp.setMonetaryDecimalSeparator(',')
    formatRp.setGroupingSeparator('.')

    idr.setDecimalFormatSymbols(formatRp)
    return idr.format(this)
}

fun String.defaultZero(): Int = if (trim().isEmpty()) 0 else this.toInt()
