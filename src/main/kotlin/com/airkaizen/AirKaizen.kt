package com.airkaizen

import com.google.common.graph.MutableNetwork
import com.google.common.graph.NetworkBuilder
import java.util.*


class AirKaizen {

    val flightNetwork: MutableNetwork<String, String> = NetworkBuilder.undirected().build()
    private val allRoutes: MutableSet<List<String>> = hashSetOf()

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

    fun findRoutes(city1: String, city2: String, maxLayovers: Int): MutableSet<List<String>> {
        val isVisited = mutableMapOf<String, Boolean>()
        val pathList = ArrayList<String>()
        val tempRoutes = ArrayList<String>()

        pathList.add(city1)

        findAllRoutesRecursively(city1, city2, isVisited, pathList, tempRoutes, maxLayovers)
        return allRoutes
    }

    private fun findAllRoutesRecursively(
        departing: String, arriving: String,
        isVisited: MutableMap<String, Boolean>,
        cityPath: MutableList<String>,
        route: MutableList<String>,
        maxLayovers: Int
    ): MutableSet<List<String>> {

        isVisited[departing] = true

        if (route.size > maxLayovers && cityPath.last() != arriving) {
            route.clear()
            return allRoutes
        }

        if (departing == arriving) {
            isVisited[departing] = false
            allRoutes.add(route.toList())
            route.clear()
            return allRoutes
        }

        val connectingCities: MutableSet<String> = try {
            flightNetwork.adjacentNodes(departing)
        } catch (e: Exception) {
            mutableSetOf()
        }

        for (connectingCity in connectingCities) {
            if (isVisited[connectingCity] == null || isVisited[connectingCity] == false) {
                cityPath.add(connectingCity)
                route.add(getRouteConnectingCities(cityPath))
                findAllRoutesRecursively(connectingCity, arriving, isVisited, cityPath, route, maxLayovers)
                cityPath.remove(connectingCity)
            }
        }

        return allRoutes
    }

    private fun getRouteConnectingCities(cityPath: MutableList<String>): String {
        return flightNetwork.edgeConnecting(
            cityPath.last(),
            cityPath[cityPath.size - 2]
        ).get()
    }


}
