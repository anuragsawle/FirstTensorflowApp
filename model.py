#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jul 24 15:57:52 2020

@author: anurag
"""


import tensorflow as tf
import numpy as np
from tensorflow import keras
from tensorflow import lite

x = np.array([-1.0,0.0,1.0,2.0,3.0,4.0],dtype = float)
y = np.array([-3.0,-1.0,1.0,3.0,5.0,7.0],dtype = float)

model = keras.Sequential([keras.layers.Dense(units=1, input_shape=[1]),
                          keras.layers.Dense(units=1,input_shape=[1])])
model.compile(optimizer='sgd',loss='mean_squared_error')

model.fit(x,y,epochs=500)
print(model.predict([10]))

keras_file = 'linear.h5'
tf.keras.models.save_model(model, keras_file)
converter = lite.TFLiteConverter.from_keras_model(model)
tfmodel = converter.convert()
open("linear.tflite","wb").write(tfmodel)
