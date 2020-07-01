package com.airkaizen

import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test

class AirKaizenTest {
    private lateinit var airKaizen: AirKaizen

    @Before
    fun setUp() {
        airKaizen = AirKaizen()
    }

    @Test
    fun addRouteCorrectlyAddsRouteAndListsCities() {
        airKaizen.addRoute("Chicago", "Detroit", "NewCode")
        val cities = airKaizen.listCities()

        assertTrue(cities!!.contains("Detroit"))
    }

    @Test
    fun getRoutesReturnsDirectFlight() {
        airKaizen.addRoute("Chicago", "New City", "KZ05")

        val flights = airKaizen.findRoutes("New City", "Chicago", 2)

        assertTrue(flights.contains(listOf("KZ05")))
    }

    @Test
    fun getRoutesHandlesNoRoutesCase() {
        val flights = airKaizen.findRoutes("Chicago", "Mackinac Island", 0)
        assertTrue(flights.isEmpty())
    }

    @Test
    fun getRoutesHandlesCitiesNotInNetwork() {
        val flights = airKaizen.findRoutes("Invalid city", "Another Invalid City", 2)
        assertTrue(flights.isEmpty())
    }

    @Test
    fun getRoutesHandlesBidirectionality() {
        val forwards = airKaizen.findRoutes("Grand Rapids", "Chicago", 2)
        val backwards = airKaizen.findRoutes("Grand Rapids", "Chicago", 2)

        assertEquals(forwards, backwards)
    }

    @Test
    fun getRoutesHandlesBidirectionalityOfMoreThanOnePossibleRoute() {
        val flightsOutbound = airKaizen.findRoutes("Mackinac Island", "Chicago", 2)

        assertTrue(flightsOutbound.contains(listOf("KZ04", "KZ01")))
        assertTrue(flightsOutbound.contains(listOf("KZ03", "KZ02")))

        val flightsInbound = airKaizen.findRoutes("Chicago", "Mackinac Island", 2)


        assertTrue(flightsInbound.contains(listOf("KZ01", "KZ04")))
        assertTrue(flightsInbound.contains(listOf("KZ02", "KZ03")))
    }

    @Test
    fun getRoutesHandlesMaxNumberOfLayovers() {
        airKaizen.addRoute("Chicago", "Connecting City", "KZ05")
        airKaizen.addRoute("Grand Rapids", "Connecting City", "KZ06")
        val flightsWithMaxTwoLayovers = airKaizen.findRoutes("Chicago", "Grand Rapids", 1)

        assertTrue(flightsWithMaxTwoLayovers.contains(listOf("KZ01")))
        assertTrue(flightsWithMaxTwoLayovers.contains(listOf("KZ05", "KZ06")))
        assertFalse(flightsWithMaxTwoLayovers.contains(listOf("KZ02", "KZ03", "KZ04")))

        val flightsWithMaxThreeLayovers = airKaizen.findRoutes("Chicago", "Grand Rapids", 2)

        assertTrue(flightsWithMaxThreeLayovers.contains(listOf("KZ01")))
        assertTrue(flightsWithMaxThreeLayovers.contains(listOf("KZ05", "KZ06")))
        assertTrue(flightsWithMaxThreeLayovers.contains(listOf("KZ02", "KZ03", "KZ04")))
    }

}
