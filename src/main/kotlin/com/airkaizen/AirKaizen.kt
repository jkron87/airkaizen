package com.airkaizen

import com.google.common.graph.MutableNetwork
import com.google.common.graph.NetworkBuilder
import java.util.*
import kotlin.collections.ArrayList

class AirKaizen {

    val flightNetwork: MutableNetwork<String, String> = NetworkBuilder.undirected().build()

    init {
        createMutableNetwork()
    }

    private fun createMutableNetwork() {
        flightNetwork.addEdge("Chicago", "Grand Rapids", "KZ01")
        flightNetwork.addEdge("Chicago", "Traverse City", "KZ02")
        flightNetwork.addEdge("Traverse City", "Mackinac Island", "KZ03")
        flightNetwork.addEdge("Grand Rapids", "Mackinac Island", "KZ04")
    }

    fun addRoute(city1: String, city2: String, routeCode: String): MutableNetwork<String, String> {
        flightNetwork.addEdge(city1, city2, routeCode)
        return flightNetwork
    }

    fun listCities(): MutableSet<String>? {
        return flightNetwork.nodes()
    }

    fun findRoutes(departing: String, arriving: String): MutableSet<List<String>> {
        val routes = mutableSetOf<List<String>>()

        val directRouteConnecting: Optional<String>?

        try {
            directRouteConnecting = flightNetwork.edgeConnecting(departing, arriving)
        } catch (e: Exception) {
            return routes
        }

        if (directRouteConnecting.isPresent) {
            routes.add(listOf(directRouteConnecting.get()))
        }

        val connectingCities = flightNetwork.adjacentNodes(departing)
        for (connectingCity in connectingCities) {
            val layoverConnectingCities = flightNetwork.adjacentNodes(connectingCity)
            if (layoverConnectingCities.contains(arriving)) {
                val route = ArrayList<String>()
                val firstLeg = flightNetwork.edgeConnecting(departing, connectingCity)
                val secondLeg = flightNetwork.edgeConnecting(connectingCity, arriving)
                route.add(firstLeg.get())
                route.add(secondLeg.get())
                routes.add(route)
            }
        }

        return routes
    }
}
