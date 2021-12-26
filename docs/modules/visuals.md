# Visuals API
An API for Minecraft visuals (scoreboards, holograms, ...).

## Visual
This is the base class of a Minecraft visual. Every other class in this API extends this class.  

* has a unique UUID for identification (can be retrieved with `getUuid()`)
* has a title (`title(Component/ComponentLike)`)
* works on a viewer basis (`addViewer(PlayerWrapper)`, `removeViewer(PlayerWrapper)`, `getViewers()`, `clearViewers()`, `hasViewers()`, `isVisibleToPlayer(PlayerWrapper)`)
* can be shown or hidden (`show()`, `hide()`, `destroy()`, `isShown()`)

## LocatableVisual
A `Visual` with a fixed location.  

* holds a location (`getLocation()`, `setLocation(LocationHolder)`)
* has a specific squared view distance (`getViewDistance()`, `setViewDistance(int)`)
* is spawnable (`spawn()`)

## TouchableVisual
A `LocatableVisual` which can be interacted with by a player.  

* can be touchable (`isTouchable()`, `setTouchable(boolean)`)
* interactions with it can have a cooldown (`getClickCoolDown()`, `setClickCoolDown(long)`)
* is bound to an entity (`hasId(int)`)

## LinedVisual
A `Visual` which can have lines.

`getLines()`, `setLines(...)`, `getLineByIdentifier(String)`, `newLine(...)`, `removeLine(...)`, `replaceLine(...)`, `firstLine(...)`, `bottomLine(...)`

## DatableVisual
A `Visual` which has a `DataContainer` bound to it.

`getData()`, `setData(DataContainer)`, `hasData()`