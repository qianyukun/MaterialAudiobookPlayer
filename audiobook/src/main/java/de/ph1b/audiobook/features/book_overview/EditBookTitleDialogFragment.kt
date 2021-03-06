package de.ph1b.audiobook.features.book_overview

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import de.ph1b.audiobook.Book
import de.ph1b.audiobook.R
import de.ph1b.audiobook.injection.App
import de.ph1b.audiobook.persistence.BookChest
import javax.inject.Inject

/**
 * Simple dialog for changing the name of a book

 * @author Paul Woitaschek
 */
class EditBookTitleDialogFragment : DialogFragment() {

    @Inject lateinit var bookChest: BookChest

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        App.component().inject(this)

        val presetName = arguments.getString(NI_PRESET_NAME)
        val bookId = arguments.getLong(NI_BOOK_ID)

        return MaterialDialog.Builder(activity)
                .title(R.string.edit_book_title)
                .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT)
                .input(getString(R.string.bookmark_edit_hint), presetName, false) { materialDialog, charSequence ->
                    val newText = charSequence.toString()
                    if (newText != presetName) {
                        bookChest.bookById(bookId)?.copy(name = newText)?.let {
                            bookChest.updateBook(it)
                        }
                    }
                }
                .positiveText(R.string.dialog_confirm)
                .build()
    }

    companion object {

        val TAG: String = EditBookTitleDialogFragment::class.java.simpleName
        private val NI_PRESET_NAME = "niPresetName"
        private val NI_BOOK_ID = "niBookId"

        fun newInstance(book: Book): EditBookTitleDialogFragment {
            val args = Bundle()
            args.putString(NI_PRESET_NAME, book.name)
            args.putLong(NI_BOOK_ID, book.id)

            val dialog = EditBookTitleDialogFragment()
            dialog.arguments = args
            return dialog
        }
    }
}
