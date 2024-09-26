import cv2
import os

# 동영상 파일 경로와 이미지를 저장할 폴더 경로 설정
video_path = '20240923_143435.mp4'  # 변환할 MP4 파일 경로
output_dir = 'frames'          # 프레임을 저장할 디렉토리 경로

# 출력 디렉토리가 존재하지 않으면 생성
if not os.path.exists(output_dir):
    os.makedirs(output_dir)

# 동영상 파일 열기
cap = cv2.VideoCapture(video_path)

# 프레임 수와 FPS 얻기
fps = cap.get(cv2.CAP_PROP_FPS)
total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))

print(f"FPS: {fps}, Total frames: {total_frames}")

frame_count = 0

# 동영상의 모든 프레임을 읽고 저장
while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break

    # 파일 이름을 'frame0001.jpg', 'frame0002.jpg' 형식으로 저장
    frame_filename = os.path.join(output_dir, f"mapleleaf{frame_count:04d}.jpg")
    cv2.imwrite(frame_filename, frame)
    
    frame_count += 1

    # 진행 상황 출력
    print(f"Saved {frame_filename}")

# 동영상 파일 닫기
cap.release()
print(f"총 {frame_count}개의 프레임이 저장되었습니다.")
