package frena.id.service

import android.app.Service
import android.app.*
import frena.id.manager.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseService: Service() {

    protected val coroutineScope = CoroutineScope(Dispatchers.Main.immediate);

    protected val serviceComponent: ServiceComponent by lazy {
        (application as MyApplication)
            .applicationComponent
            .newServiceComponent(ServiceModule(this))
    }
}