package com.urosjarc.e2e

import com.urosjarc.architect.core.Service


@Service
interface WeatherService {
    fun getPrediction(): String
}

class ArsoWeatherService: WeatherService {
    override fun getPrediction(): String {
        TODO("Not yet implemented")
    }

}

@Service
interface TimeService {
    fun getTime(): String
}

class AtomicTimeService: TimeService {
    override fun getTime(): String {
        TODO("Not yet implemented")
    }

}
