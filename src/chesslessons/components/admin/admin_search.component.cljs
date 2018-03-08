(ns chesslessons.components.admin_search.component
	(:require
		[chesslessons.search-model :refer [on_search_change]]))

(def log (.-log js/console))



(defn render_admin_search []
	[:form.form-inline.my-2.my-lg-0

	 [:span.d-inline-block {:tab-index "0" :data-toggle "tooltip" }
		[:input.form-control.mr-sm-2 {:type "search"
																	:placeholder "Search..."
																	:aria-label "Search"
																	:on-change on_search_change
																	:title "Enter name or email"}]]
	 ])
