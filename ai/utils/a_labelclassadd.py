import os

# 기존 데이터셋이 저장된 경로
dataset_dir = './'  # 라벨링된 .txt 파일들이 저장된 경로

# 수정된 라벨을 저장할 디렉토리 (원본에 덮어쓰지 않으려면 별도 디렉토리를 지정하세요)
output_dir = './'
os.makedirs(output_dir, exist_ok=True)

# 모든 .txt 파일 순회
for label_file in os.listdir(dataset_dir):
    if label_file.endswith('.txt'):
        label_path = os.path.join(dataset_dir, label_file)
        output_path = os.path.join(output_dir, label_file)

        with open(label_path, 'r') as f:
            lines = f.readlines()

        # 파일이 비어 있으면 그대로 저장
        if not lines:
            with open(output_path, 'w') as f:
                f.write('')
            continue

        # 새로운 라벨 파일 저장
        with open(output_path, 'w') as f:
            for line in lines:
                # 각 줄의 첫 번째 숫자는 클래스 번호
                parts = line.strip().split()
                if parts:  # 내용이 있는 경우에만 처리
                    class_number = int(parts[0]) + 3  # 첫 번째 숫자에 3을 더함
                    new_line = f"{class_number} " + " ".join(parts[1:]) + "\n"
                    f.write(new_line)
