from flask import Flask, request, jsonify
from tensorflow import keras
import model as util
import numpy as np
import os

os.environ["CUDA_VISIBLE_DEVICES"] = "-1"
app = Flask(__name__)

model = keras.models.load_model("resources/model2.h5")

messages = {"0": "Stress message",
            "1": "Sad message"}


@app.route('/predict', methods=["POST"])
def predict():
    if request.method == "POST":
        message = request.get_json()["message"]
        data = [util.data_processing.process_sentence(message)]
        if "stress" in data[0] or "anxious" in data[0]:
            return jsonify(prediction="0",
                           confidence="1.0",
                           message=messages["0"])
        elif "sad" in data[0] or "depress" in data[0]:
            return jsonify(prediction="1",
                           confidence="1.0",
                           message=messages["1"])
        else:
            input_vector = util.convert_to_input_vector(data)
            prediction = model.predict(input_vector).flatten()
            print("Prediction: ", np.argmax(prediction), "Confidence: ", prediction[np.argmax(prediction)] * 100)
            print(prediction * 100)

            if prediction[np.argmax(prediction)] > 0.78:
                return jsonify(prediction=str(np.argmax(prediction)),
                               confidence=str(prediction[np.argmax(prediction)]),
                               message=messages[str(np.argmax(prediction))])
            else:
                return jsonify(prediction=str(np.argmax(prediction)),
                               confidence=str(prediction[np.argmax(prediction)]),
                               message="NONE")


if __name__ == "__main__":
    app.run(debug=True)
