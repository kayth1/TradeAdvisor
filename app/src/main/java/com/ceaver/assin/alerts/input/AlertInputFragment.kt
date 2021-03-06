package com.ceaver.assin.alerts.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ceaver.assin.R

class AlertInputFragment : Fragment() {

    private lateinit var viewModel: AlertViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val args = AlertInputFragmentArgs.fromBundle(requireArguments())
//        viewModel = viewModels<AlertViewModel> { AlertViewModel.Factory(args.alert) }.value
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        bindSymbol()
//        bindReference()
//        bindAlert()
        return inflater.inflate(R.layout.activity_alert_input, container, false)
    }

    override fun onStart() {
        super.onStart()

//        bindActions()
//        observeStatus()
//        bindViewLogic()
//        bindFieldValidators()
    }

//    private fun bindActions() {
//        alertSaveButton.setOnClickListener { onSaveClick() }
//    }
//
//    private fun bindSymbol() {
//        viewModel.title.observe(viewLifecycleOwner, Observer {
//            val adapter = ArrayAdapter<Title>(requireContext(), android.R.layout.simple_spinner_item)
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            alertSymbolText.setAdapter(adapter)
//            adapter.addAll(it!!); updateSpinnerFields()
//        })
//    }
//
//    private fun bindReference() {
//        viewModel.reference.observe(viewLifecycleOwner, Observer {
//            val adapter = ArrayAdapter<Title>(requireContext(), android.R.layout.simple_spinner_item)
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            alertReferenceText.setAdapter(adapter)
//            adapter.addAll(it!!); updateSpinnerFields()
//        })
//    }
//
//    private fun bindAlert() {
//        viewModel.alert.observe(viewLifecycleOwner, Observer {
//            val adapter = ArrayAdapter<Title>(requireContext(), android.R.layout.simple_spinner_item)
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            alertReferenceText.setAdapter(adapter)
//            bindFields(it!!); alertSaveButton.isEnabled = true
//        })
//    }
//
//    private fun bindViewLogic() {
//        fun updatePrice() {
//            lifecycleScope.launch {
//                if (viewModel.isNew() && alertSymbolText.selectedItem != null && alertReferenceText.selectedItem != null) {
//                    val symbol = alertSymbolText.selectedItem as Title
//                    val reference = alertReferenceText.selectedItem as Title
//                    viewModel.lookupPrice(symbol, reference) {
//                        alertSourceEditText.setText(it.first.format(reference.symbol))
//                        alertTargetEditText.setText(it.second.format(reference.symbol))
//                    }
//                }
//            }
//        }
//
//        fun updateUnit() {
//            val symbol = alertReferenceText.selectedItem as Title
//            startUnitTextView.text = symbol.symbol
//            targetUnitTextView.text = symbol.symbol
//        }
//        alertSymbolText.onItemSelectedListener = SpinnerSelectionListener { updatePrice(); checkSaveButton() }
//        alertReferenceText.onItemSelectedListener = SpinnerSelectionListener { updateUnit(); updatePrice(); checkSaveButton() }
//    }
//
//    private fun bindFields(alert: Alert) {
//        alertSourceEditText.setText(alert.source.toPlainString())
//        alertTargetEditText.setText(alert.target.toPlainString())
//
//        updateSpinnerFields()
//
//        alertSymbolText.isEnabled = alert.isNew()
//        alertReferenceText.isEnabled = alert.isNew()
//    }
//
//    private fun updateSpinnerFields() {
//        if (viewModel.alert.value != null && viewModel.title.value != null && viewModel.reference.value != null) {
//            alertSymbolText.setSelection(viewModel.title.value!!.indexOf(viewModel.alert.value!!.title))
//            alertReferenceText.setSelection(viewModel.reference.value!!.indexOf(viewModel.alert.value!!.referenceTitle))
//        }
//    }
//
//    private fun observeStatus() {
//        viewModel.status.observe(this, Observer {
//            when (it) {
//                AlertViewModel.AlertInputStatus.START_SAVE -> onStartSave()
//                AlertViewModel.AlertInputStatus.END_SAVE -> onEndSave()
//                null -> throw IllegalStateException()
//            }
//        })
//    }
//
//    private fun onSaveClick() {
//        val symbol = alertSymbolText.selectedItem as Title
//        val reference = alertReferenceText.selectedItem as Title
//        val startPrice = alertSourceEditText.text.toString().toBigDecimal()
//        val targetPrice = alertTargetEditText.text.toString().toBigDecimal()
//        viewModel.onSaveClick(symbol, reference, startPrice, targetPrice)
//    }
//
//    private fun onStartSave() {
//        alertSaveButton.isEnabled = false // TODO Disable inputs fields as well
//    }
//
//    private fun onEndSave() {
//        findNavController().navigateUp()
//    }
//
//    private fun bindFieldValidators() {
//        alertSourceEditText.registerInputValidator({ s -> (s.replace(".", "").length >= 1) }, "Please enter quantity")
//        alertTargetEditText.registerInputValidator({ s -> ((s.replace(".", "").length >= 1) && (s.toDouble() > 0.0)) }, "Please enter quantity")
//        alertSourceEditText.afterTextChanged { checkSaveButton() }
//        alertTargetEditText.afterTextChanged { checkSaveButton() }
//    }
//
//    private fun checkSaveButton() {
//        alertSaveButton.isEnabled = alertSourceEditText.error == null && alertTargetEditText.error == null && alertSymbolText.selectedItem != alertReferenceText.selectedItem
//    }
}
