from flask import Flask, request, jsonify
from tensorflow import keras
import model as util
import numpy as np
import os


os.environ["CUDA_VISIBLE_DEVICES"] = "-1"
app = Flask(__name__)

model = keras.models.load_model("resources/model2.h5")

messages = {"0": "It's okay! Everybody gets stressed sometimes. I always find meditation, exercise, and/or "
                 "\"unplugging\" from electronics to be helpful.",
            "1": "Sometimes, things get hard. Whenever you find yourself doubting how far you can go, "
                 "just remember how far you have come. "
                 "Remember everything you have faced, all the battles you have won, "
                 "and all the fears you have overcome."}


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

            if prediction[np.argmax(prediction)] > 0.75:
                return jsonify(prediction=str(np.argmax(prediction)),
                               confidence=str(prediction[np.argmax(prediction)]),
                               message=messages[str(np.argmax(prediction))])
            else:
                return jsonify(prediction=str(np.argmax(prediction)),
                               confidence=str(prediction[np.argmax(prediction)]),
                               message="Unfortunately, I am not advanced enough to understand your message. "
                                       "However, feel free to continue messaging me! "
                                       "Even if I don't understand your message, "
                                       "I will make sure to listen to your worries. :)")


if __name__ == "__main__":
    app.run(debug=False, host="0.0.0.0", port=5000)
