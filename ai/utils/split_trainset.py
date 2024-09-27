import os
import shutil
from sklearn.model_selection import train_test_split

# 데이터 파일들이 있는 디렉토리 경로
data_dir = 'C:/Users/SSAFY/Downloads/todotrain/image'  # 이 경로를 정확하게 설정하세요

# 이미지와 라벨 파일들을 저장할 새로운 디렉토리 경로
train_dir = os.path.join(data_dir, 'train')
val_dir = os.path.join(data_dir, 'val')
test_dir = os.path.join(data_dir, 'test')

# 디렉토리 생성
os.makedirs(train_dir, exist_ok=True)
os.makedirs(val_dir, exist_ok=True)
os.makedirs(test_dir, exist_ok=True)

# 파일 목록 가져오기 (다양한 확장자를 허용하도록 수정)
files = [f for f in os.listdir(data_dir) if f.lower().endswith(('.jpg', '.jpeg', '.png'))]

# 파일들이 실제로 있는지 확인
if not files:
    raise ValueError("지정된 디렉토리에 이미지 파일이 없습니다. 경로를 확인하세요.")

# 파일들 무작위로 섞기
train_files, temp_files = train_test_split(files, test_size=0.3, random_state=42)
val_files, test_files = train_test_split(temp_files, test_size=0.5, random_state=42)

# 파일 이동 함수 정의
def move_files(files, destination_dir):
    for file_name in files:
        # 이미지 파일 이동
        shutil.move(os.path.join(data_dir, file_name), os.path.join(destination_dir, file_name))
        
        # 대응되는 텍스트 파일 이동 (확장자만 변경)
        label_name = file_name.rsplit('.', 1)[0] + '.txt'
        label_path = os.path.join(data_dir, label_name)
        
        # 라벨 파일이 존재하는지 확인하고 이동
        if os.path.exists(label_path):
            shutil.move(label_path, os.path.join(destination_dir, label_name))
        else:
            print(f"Warning: 라벨 파일이 존재하지 않음: {label_name}")

# 파일 이동
move_files(train_files, train_dir)
move_files(val_files, val_dir)
move_files(test_files, test_dir)

print(f'Train files: {len(train_files)}')
print(f'Val files: {len(val_files)}')
print(f'Test files: {len(test_files)}')
