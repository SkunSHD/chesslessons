(ns chesslessons.admin-model
	(:require
		[reagent.core :refer [atom cursor]]
))


; ==================
; Atoms
(defonce admin (atom nil))


; ==================
; Public
(defn set_admin [new_admin]
	(reset! admin new_admin))
