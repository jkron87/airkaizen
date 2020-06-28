package com.airkaizen

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
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
        airKaizen.addRoute("Chicago", "Detroit", "KZ05")
        val cities = airKaizen.listCities()

        assertTrue(cities!!.contains("Detroit"))
    }

    @Test
    fun getRoutesReturnsDirectFlight() {
        airKaizen.addRoute("Chicago", "Detroit", "KZ05")
        val flights = airKaizen.findRoutes("Chicago", "Detroit")

        assertTrue(flights.contains(listOf("KZ05")))
    }

    @Test
    fun getRoutesHandlesNoRoutesCase() {
        val flights = airKaizen.findRoutes("Traverse City", "Detroit")
        assertTrue(flights.isEmpty())
    }

    @Test
    fun getRoutesHandlesCitiesNotInNetwork() {
        val flights = airKaizen.findRoutes("Invalid city", "Another invalid city")
        assertTrue(flights.isEmpty())
    }

    @Test
    fun getRoutesHandlesBidirectionality() {
        val forwards = airKaizen.findRoutes("Grand Rapids", "Chicago")
        val backwards = airKaizen.findRoutes("Grand Rapids", "Chicago")

        assertEquals(forwards, backwards)
    }

    @Test
    fun getRoutesReturnsMoreThanOnePossibleRoute() {
        val flights = airKaizen.findRoutes("Chicago", "Mackinac Island")

        assertTrue(flights.contains(listOf("KZ01", "KZ04")))
        assertTrue(flights.contains(listOf("KZ02", "KZ03")))
    }


    @Test
    fun getRoutesHandlesBidirectionalityOfMoreThanOnePossibleRoute() {
        val flights = airKaizen.findRoutes("Mackinac Island", "Chicago")

        assertTrue(flights.contains(listOf("KZ04", "KZ01")))
        assertTrue(flights.contains(listOf("KZ03", "KZ02")))
    }

    @Test
    fun getRoutesDoesNotReturnRoutesWithMoreThanTwoLegs() {
        airKaizen.addRoute("Mackinac Island", "Bois Blanc Island", "KZ06")
        val flights = airKaizen.findRoutes("Chicago", "Bois Blanc Island")

        assertTrue(flights.isEmpty())
    }
}
