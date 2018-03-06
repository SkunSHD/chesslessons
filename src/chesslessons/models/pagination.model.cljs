(ns chesslessons.pagination-model
	(:require
		[reagent.core :refer [atom cursor]]
		))


(def log (.-log js/console))


; ==================
; Atoms
(defonce pagination (atom {
						 :current 0
						 :display 5
						 }))

