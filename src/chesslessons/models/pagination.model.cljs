(ns chesslessons.pagination-model
	(:require
		[reagent.core :refer [atom cursor]]
		))


(def log (.-log js/console))


; ==================
; Atoms
(defonce pagination (atom {
							  :display 5
							  :current { :visitors 0 :deleted_visitors 0 :anonymous_visitors 0 }
							  }))


; ==================
; Public
(defn set_current_pagination_page [number tab]
	(swap! pagination assoc-in [:current tab] number))