(ns ch.d4span.taskstewart.arch-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io])
  (:import (com.tngtech.archunit.library.plantuml PlantUmlArchCondition PlantUmlArchCondition$Configurations PlantUmlArchCondition$Configuration)
           (com.tngtech.archunit.core.importer ClassFileImporter))
  (:gen-class))

(defn arch-fixture [test]
  (let [config-class (class (PlantUmlArchCondition$Configurations/consideringAllDependencies))
        class-loader (. config-class getClassLoader)]
    ;(with-bindings {clojure.lang.Compiler/LOADER class-loader}
    (. (Thread/currentThread) setContextClassLoader class-loader)
    (test)))

(use-fixtures :each arch-fixture)

(deftest arch-test
  ;(testing "Architecture"
    (let [diagram-url (io/resource "taskstewart.puml")
          classes (new ClassFileImporter)
          config (PlantUmlArchCondition$Configurations/consideringAllDependencies)
          should-clause (PlantUmlArchCondition/adhereToPlantUmlDiagram diagram-url config)]
      (. classes importPackages "ch.d4span.taskstewart")
      (is (. classes should should-clause))))