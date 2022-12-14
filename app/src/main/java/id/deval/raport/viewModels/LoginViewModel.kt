package id.deval.raport.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.deval.raport.db.Repository
import id.deval.raport.db.models.Account
import id.deval.raport.utils.wrappers.GlobalWrapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {

    private lateinit var mutableUserLogin : MutableLiveData<Response<GlobalWrapper<Account>>>
    private lateinit var mutableUserLogout : MutableLiveData<Response<Unit>>
    var token : String? = null

    fun login(account: Account): LiveData<Response<GlobalWrapper<Account>>> {
        mutableUserLogin = MutableLiveData()
        GlobalScope.launch {
            val response = repository.login(account)
            token = response.body()?.token
            mutableUserLogin.postValue(response)
        }
        return mutableUserLogin
    }

    fun logout(): LiveData<Response<Unit>>{
        mutableUserLogout = MutableLiveData()
        GlobalScope.launch {
            val response = repository.logout()
            mutableUserLogout.postValue(response)
        }
        return mutableUserLogout
    }
}