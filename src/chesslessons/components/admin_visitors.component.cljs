(ns chesslessons.components.admin-visitors.components
    (:require
        [reagent.core :refer [atom cursor]]
;       Models
        [chesslessons.visitors-model :refer [visitors]]
        )
    )



(def log (.-log js/console))


; ==================
; Atoms
(defonce tab (atom "")) ; "Visitors" "Deleted"
;(defonce visitors (atom '()))
;(defonce deleted_visitors (atom '()))



(defn- -toggle_active_class [new_el]
    (let [old_elem_query (.querySelector js/document ".nav-link.active")]
        (.remove (.-classList old_elem_query) "active")
        (.add (.-classList new_el) "active")))



(defn- -on_tab_click_handler [event tab_name]
    (.preventDefault event)
    (reset! tab tab_name)
    (-toggle_active_class (.-target event))
    )


(defn render_tabs []
    [:div.card-header
     [:ul.nav.nav-tabs.card-header-tabs
      [:li.nav-item
       [:a.nav-link.active {:on-click #(-on_tab_click_handler % "Visitors")} "Visitors"]]
      [:li.nav-item
       [:a.nav-link {:on-click #(-on_tab_click_handler % "Deleted")} "Deleted"]]]
     ]
    )

(defn set_deleted_visitors []
    )


(defn render []
    [:div
     [:h1 "Visitors:"]
     [:div.card.text-center
      [render_tabs]
      [:div.card-body
        [:p (str "Visitors number: " (count @visitors))]
       ;      [render_filters]
;             [render_admin_visitors]
       ]
      ]
     ]
    )