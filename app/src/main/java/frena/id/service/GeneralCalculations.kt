package com.orbitalsonic.forgroundservicewithnotification

object GeneralCalculations {


    /**
     * we just add one minute extra
     * to maintain calculations
     */

    //for week exercise fragment
    private fun getMinuteTime(timeSeconds:Int):Int{
        var timeMint = timeSeconds/60
        timeMint+=1
        return timeMint
    }

    /**
     *  we want to get time in this format
     *  00:00  min:sec
     *  00:00:00 hr:min:sec
     */
    private fun getTimeString(mTime: Int):String  {
        val hours:Int = mTime / 3600
        var remainder:Int = mTime - hours * 3600
        val minutes:Int = remainder / 60
        remainder -= minutes * 60
        val seconds:Int = remainder

       val hourString = if (hours>9){
           "$hours"
       }else{
           "0$hours"
       }

       val minuteString = if (minutes>9){
           "$minutes"
       }else{
           "0$minutes"
       }

       val secString = if (seconds>9){
           "$seconds"
       }else{
           "0$seconds"
       }

       return if (hours<=0){
           "$minuteString:$secString"
       }else{
           "$hourString:$minuteString:$secString"
       }
    }

    /**
     *  we want to get time in this format
     *  00:00:00 hr:min:sec
     */
    fun getStopWatchTimer(mTime: Long):String  {
        val hours:Int = (mTime / 3600).toInt()
        var remainder:Int = (mTime - hours * 3600).toInt()
        val minutes:Int = remainder / 60
        remainder -= minutes * 60
        val seconds:Int = remainder

        val hourString = if (hours>9){
            "$hours"
        }else{
            "0$hours"
        }

        val minuteString = if (minutes>9){
            "$minutes"
        }else{
            "0$minutes"
        }

        val secString = if (seconds>9){
            "$seconds"
        }else{
            "0$seconds"
        }

        return "$hourString:$minuteString:$secString"
    }

}