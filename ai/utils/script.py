from ultralytics import YOLO
import requests
import json

def send_mattermost_message(webhook_url, message):
    headers = {
        'Content-Type': 'application/json'
    }
    data = {
        'text': message
    }
    response = requests.post(webhook_url, headers=headers, data=json.dumps(data))

    if response.status_code != 200:
        raise ValueError(f"Request to Mattermost returned an error {response.status_code}, the response is:\n{response.text}")

# Mattermost Webhook URL
webhook_url = "https://meeting.ssafy.com/hooks/6b6qtb6qajbc8mwc1e4557o7zh"

# YOLO 모델 로드 및 학습
model = YOLO("yolov8n.pt")
results = model.train(data="data.yaml", epochs=300, batch=240, device=4, patience=100, augment=True)

# 학습 결과 메시지 초기화
message = "###### Training has been completed on GPU server.\n"

# mAP 값이 존재하는지 확인
if results.maps is not None and len(results.maps) > 0:  
    message += f"**Best mAP@0.5:** {results.maps[0]:.4f}\n"
    message += f"**Best mAP@0.5:0.95:** {results.maps.mean():.4f}\n"

# Precision과 Recall 값 추가
if hasattr(results, 'box') and results.box:
    precision, recall = results.box.mean_results()[:2]
    message += f"**Precision:** {precision:.4f}\n"
    message += f"**Recall:** {recall:.4f}\n"

# 학습 손실 추가
if hasattr(results, 'loss') and len(results.loss) > 0:
    message += f"**Training Loss:** {results.loss[-1]:.4f}\n"

# 검증 손실 추가
if hasattr(results, 'val_loss') and len(results.val_loss) > 0:
    message += f"**Validation Loss:** {results.val_loss[-1]:.4f}\n"


# Mattermost로 메시지 보내기
send_mattermost_message(webhook_url, message)

