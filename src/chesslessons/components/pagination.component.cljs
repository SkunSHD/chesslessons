(ns chesslessons.components.pagination.components
	(:require
; 		Models
		[chesslessons.pagination-model :refer [pagination]]
		[chesslessons.visitors-model :refer [visitors]]
		))

(def log (.-log js/console))



(defn visitors_size []
	(count @visitors))




(defn render_pagination_item [index current]
	[:li.page-item {:class (if (= index current) "active") :key index}
	 [:a.page-link {:on-click #(log "page " index)} (str (+ index 1))]])


(defn render_pagination_items []
;	(log "(range (/ (visitors_size) (:range @pagination)))" (map (fn [index] [:li.page-item {:key index} [:a.page-link {:href "#"} (str (+ index 1))]]) (range (/ (visitors_size) (:display @pagination)))))
	(log "(range (/ (visitors_size) (:range @pagination)))" (range (/ (visitors_size) (:display @pagination))))
	[:ul.pagination
	 (let [display (:display @pagination) current (:current @pagination)]
		 (for [index (range (/ (visitors_size) display))]
		 ^{:key index} (render_pagination_item index current)
		 ))

	 ]
	)


(defn render []
	(if (> (visitors_size) (:display @pagination))
			 [:nav
				[render_pagination_items]
		 ])
	)
