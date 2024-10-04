import os

# 수정할 파일들이 있는 디렉토리 경로
directory = './'  # 라벨 파일이 있는 디렉토리 경로로 변경

# 디렉토리 내 모든 파일을 순회
for filename in os.listdir(directory):
    if filename.endswith(".txt"):  # .txt 파일만 처리
        file_path = os.path.join(directory, filename)

        # 파일을 열어서 내용을 읽음
        with open(file_path, 'r') as file:
            lines = file.readlines()

        # 각 라인의 첫 번째 숫자를 0에서 16으로 변경
        new_lines = []
        for line in lines:
            parts = line.strip().split()
            if parts[0] == '0':  # 클래스 번호가 0일 때만 변경
                parts[0] = '18'  # 클래스 번호를 16으로 변경
            new_lines.append(" ".join(parts))

        # 변경된 내용을 다시 파일에 저장
        with open(file_path, 'w') as file:
            file.write("\n".join(new_lines))

print("클래스 번호 변경 완료!")
