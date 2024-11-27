package frena.id.service

import dagger.Subcomponent

@Subcomponent(modules = [ServiceModule::class])
interface ServiceComponent {
    fun inject(service: ForegroundService)
  //  fun inject(service: ComposeOverlayService)
}