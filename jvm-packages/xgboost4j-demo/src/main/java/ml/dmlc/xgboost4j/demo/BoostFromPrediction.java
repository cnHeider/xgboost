/*
 Copyright (c) 2014 by Contributors

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package ml.dmlc.xgboost4j.demo;

import java.util.HashMap;

import ml.dmlc.xgboost4j.Booster;
import ml.dmlc.xgboost4j.DMatrix;
import ml.dmlc.xgboost4j.XGBoost;
import ml.dmlc.xgboost4j.XGBoostError;

/**
 * example for start from a initial base prediction
 *
 * @author hzx
 */
public class BoostFromPrediction {
  public static void main(String[] args) throws XGBoostError {
    System.out.println("start running example to start from a initial prediction");

    // load file from text file, also binary buffer generated by xgboost4j
    DMatrix trainMat = new DMatrix("../../demo/data/agaricus.txt.train");
    DMatrix testMat = new DMatrix("../../demo/data/agaricus.txt.test");

    //specify parameters
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("eta", 1.0);
    params.put("max_depth", 2);
    params.put("silent", 1);
    params.put("objective", "binary:logistic");

    //specify watchList
    HashMap<String, DMatrix> watches = new HashMap<String, DMatrix>();
    watches.put("train", trainMat);
    watches.put("test", testMat);

    //train xgboost for 1 round
    Booster booster = XGBoost.train(params, trainMat, 1, watches, null, null);

    float[][] trainPred = booster.predict(trainMat, true);
    float[][] testPred = booster.predict(testMat, true);

    trainMat.setBaseMargin(trainPred);
    testMat.setBaseMargin(testPred);

    System.out.println("result of running from initial prediction");
    Booster booster2 = XGBoost.train(params, trainMat, 1, watches, null, null);
  }
}
