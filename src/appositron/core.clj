(ns appositron.core
  (:gen-class)
  (:use [analemma svg charts xml]
        [tikkba swing dom]
        tikkba.utils.xml)
  (:require [tikkba.utils.dom :as dom]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def page-width  600)
(def page-height 600)

;; Array of azimuth, declination
;;  Multiple domains
(def aperture
  [[  0.0 10.0]
    [ 10.0 10.0]
    [ 20.0 10.0]
    [ 30.0 10.0]
    [ 40.0 10.0]
    [ 50.0 10.0]
    [ 60.0 10.0]
    [ 70.0 10.0]
    [ 80.0 10.0]
    [ 90.0 10.0]
    [100.0 10.0]
    [110.0 10.0]
    [120.0 10.0]
    [130.0 10.0]
    [140.0 10.0]
    [150.0 10.0]
    [160.0 10.0]
    [170.0 10.0]
    [180.0 10.0]
    [190.0 10.0]
    [200.0 10.0]
    [210.0 10.0]
    [220.0 10.0]
    [230.0 10.0]
    [240.0 10.0]
    [250.0 10.0]
    [260.0 10.0]
    [270.0 10.0]
    [280.0 10.0]
    [290.0 10.0]
    [300.0 10.0]
    [310.0 10.0]
    [320.0 10.0]
    [330.0 10.0]
    [340.0 10.0]
    [350.0 10.0]
    ])

;; Aperature channels
(def channel-colours
  ["#ff0000"  ; Red
   "#00ff00"  ; Green
   "#0000ff"  ; Blue
   ])

;;  Chanels Array
;;  -> Aperture Segment Array
;;     -> Segment
;;        -> Boundry Points
(def aperture-channels
  [[   0.0  20.0    5.0  21.0]
   [   5.0  21.0   10.0  22.0]
   [  10.0  22.0   30.0  90.0]
   [  30.0  90.0   25.0  90.0]
   [  25.0  90.0   20.0  90.0]
   [  20.0  90.0   15.0  90.0]
   [  15.0  90.0   10.0  90.0]
   [  10.0  90.0    5.0  90.0]
   [   5.0  90.0    0.0  90.0]
   [   0.0  90.0   -5.0  90.0]
   [  -5.0  90.0  -10.0  90.0]
   [ -10.0  90.0  -15.0  90.0]
   [ -15.0  90.0  -20.0  90.0]
   [ -20.0  90.0  -25.0  90.0]
   [ -25.0  90.0  -30.0  90.0]
   [ -30.0  90.0  -10.0  22.0]
   [ -10.0  22.0   -5.0  21.0]
   [  -5.0  21.0    0.0  20.0]
   ]
)

(defn transform-aperture [aperture]
  (map (fn [[a b]] [(* b (Math/cos a))
                    (* b (Math/sin a))]) aperture)
  )

(defn deg2rad [degrees]
  (* (/ degrees 360) Math/PI 2)
)

(defn calc-x [r t]
  (+ (* (+ (* (* r (Math/cos (deg2rad t))))) (/ 300 90)) 300)
  )

(defn calc-y [r t]
  (+ (* (+ (* (* r (Math/sin (deg2rad t))))) (/ 300 90)) 300)
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn create-aperture-full-svg
  ""
  []
  (let [center-x (/ page-width 2)
        center-y (/ page-height 2) 
        TwoPi (* Math/PI 2)
        ]
  (svg
   (apply group
          (-> (rect 0 0 page-width page-height :id "rect0")
              (style :fill "white" :stroke "blue" :stroke-width 0))

          (-> (text {:x 10 :y 20} "Aperture")
              (style :font-family "Courier" :font-size 16 :fill "blue"))              

          (-> (circle (/ page-width 2) (/ page-height 2) 300 :id "circle")
              (style  :fill :none :stroke "blue" :stroke-width 1))
          (-> (circle (/ page-width 2) (/ page-height 2) 200 :id "circle")
              (style  :fill :none :stroke "blue" :stroke-width 1))
          (-> (circle (/ page-width 2) (/ page-height 2) 100 :id "circle")
              (style  :fill :none :stroke "blue" :stroke-width 1))
          
          (-> (path [:M [center-x center-y]
                     :l [300 0]
                     :M [center-x center-y]
                     :l [-300 0]
                     :M [center-x center-y]
                     :l [0 -300]
                     :M [center-x center-y]
                     :l [0 300]
                     
                     ])
              (style  :fill :none :stroke "blue" :stroke-width 1))
          (-> (text {:x (+ center-x 78)
                     :y (+ center-y 16)}
                    "30")
              (style :font-family "Arial" :font-size 16 :fill "blue"))
          (-> (text {:x (+ center-x 178)
                     :y (+ center-y 16)}
                    "60")
              (style :font-family "Arial" :font-size 16 :fill "blue"))
          (-> (text {:x (+ center-x 278)
                     :y (+ center-y 16)}
                    "90")
              (style :font-family "Arial" :font-size 16 :fill "blue"))              

          (for [[x y m n] aperture-channels]
            (let [a1 (calc-x y x)
                  a2 (calc-y y x)
                  b1 (calc-x n m)
                  b2 (calc-y n m)
                  ]
;            (circle a1 a2 2 :fill "#000000")))
              (-> (path [:M [a1 a2]
                         :L [b1 b2]])
              (style :stroke "blue" :stroke-width 1))

             )
            )

          )
   )))

(defn create-crosssection-svg
  ""
  []
  (let [center-x (/ page-width 2)
        center-y (/ page-height 2) 
        TwoPi (* Math/PI 2)
        ]
  (svg
   (-> (rect 0 0 page-width page-height :id "rect0")
       (style :fill "white" :stroke "blue" :stroke-width 0))

   ;; (-> (circle (/ page-width 2) (/ page-height 2) 300 :id "circle")
   ;;     (style  :fill :none :stroke "blue" :stroke-width 1))
   ;; (-> (circle (/ page-width 2) (/ page-height 2) 200 :id "circle")
   ;;     (style  :fill :none :stroke "blue" :stroke-width 1))
   ;; (-> (circle (/ page-width 2) (/ page-height 2) 100 :id "circle")
   ;;     (style  :fill :none :stroke "blue" :stroke-width 1))

   (-> (text {:x 10 :y 20} "Crosssection - Declination:   0.0 Aximuth:   0.0")
       (style :font-family "Courier" :font-size 16 :fill "blue"))              

   (-> (path [:M [center-x center-y]
              :l [300 0]
              :M [center-x center-y]
              :l [-300 0]
              :M [center-x center-y]
              :l [0 -300]
              :M [center-x center-y]
              :l [0 300]

              ])
       (style  :fill :none :stroke "blue" :stroke-width 1))
   (-> (text {:x (+ center-x 78)
              :y (+ center-y 16)}
             "10")
       (style :font-family "Arial" :font-size 16 :fill "blue"))
   (-> (text {:x (+ center-x 178)
              :y (+ center-y 16)}
             "20")
       (style :font-family "Arial" :font-size 16 :fill "blue"))
   (-> (text {:x (+ center-x 278)
              :y (+ center-y 16)}
             "30")
       (style :font-family "Arial" :font-size 16 :fill "blue"))              

   )))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn write-svg
  [create-svg file-svg]
  ;; Converts the SVG representation to a XML Document
  ;; and writes it to a file
  (let [doc (svg-doc (create-svg))]
    (dom/spit-xml file-svg doc
                  :indent "yes"
                  :encoding "UTF8")))

(write-svg create-aperture-full-svg "aperture-full.svg")
(write-svg create-crosssection-svg  "crosssection.svg")
