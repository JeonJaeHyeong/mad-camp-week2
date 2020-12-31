package com.example.madcampweek1

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_phone_book.view.*

class PhoneBookFragment : Fragment() {

    lateinit var mAadapter:PhoneAdapter
    var phonelist = mutableListOf<Phone>()
    var searchText = ""
    var sortText = "asc"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone_book, container, false)
        setContentView(view)
        return view
    }

    fun setContentView(view: View){
        phonelist.addAll(getPhoneNumbers(sortText, searchText))
        mAadapter = PhoneAdapter(phonelist)
        view.recycler.adapter = mAadapter
        view.recycler.layoutManager = LinearLayoutManager(context)
    }

    fun getPhoneNumbers(sort:String, searchName:String?) : List<Phone> {
        // 결과목록 미리 정의
        val list = mutableListOf<Phone>()
        // 1. 주소록 Uri - 여기서는 사용안함, 비교를 위해 작성
        //val addressUri = ContactsContract.Contacts.CONTENT_URI
        // 1. 전화번호 Uri
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        // 2.1 전화번호에서 가져올 컬럼 정의
        val projections = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            , ContactsContract.CommonDataKinds.Phone.NUMBER)
        // 2.2 조건 정의
        var wheneClause:String? = null
        var whereValues:Array<String>? = null
        // searchName에 값이 있을 때만 검색을 사용한다
        if(searchName?.isNotEmpty() ?: false) {
            wheneClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ?"
            whereValues = arrayOf("%$searchName%")
        }
        // 2.3 정렬쿼리 사용
        val optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " $sort"
        // 3. 테이블에서 주소록 데이터 쿼리
        context?.run{
            val cursor = contentResolver.query(phoneUri, projections, wheneClause, whereValues, optionSort)
            // 4. 반복문으로 아이디와 이름을 가져오면서 전화번호 조회 쿼리를 한번 더 돌린다.
            while(cursor?.moveToNext()?:false) {
                val id = cursor?.getString(0)
                val name = cursor?.getString(1)
                var number = cursor?.getString(2)
                // 개별 전화번호 데이터 생성
                val phone = Phone(id, name, number)
                // 결과목록에 더하기
                list.add(phone)
            }
        }

        // 결과목록 반환
        return list
    }
}