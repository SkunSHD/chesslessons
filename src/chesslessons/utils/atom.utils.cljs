(ns chesslessons.atom.utils
	(:require
		[reagent.core :refer [atom cursor]]
))


(def log (.-log js/console))
(def group_collapsed (.-groupCollapsed js/console))
(def group_end (.-groupEnd js/console))


; ==================
; Private
(defn- -atom_logger [key atom old new]
	(group_collapsed (str "%c🦄🌈 " "ATOM-CHANGED: " key) "color: #03a528;")
	(log old)
	(log "to")
	(log new)
	(group_end))


; ==================
; Pulbic
; '%c🦄🌈 ' + event.name, 'color: #03a528;'
(defn add_watch! [name atom]
	(add-watch atom name -atom_logger))


(defn atom! [name value]
	(let [_atom (atom value)]
		(add_watch! name _atom)
		_atom))