import os

# 현재 디렉토리의 모든 파일 목록 가져오기
files = os.listdir()

# 모든 .txt 파일에 대해 처리
for file in files:
    if file.endswith('.txt'):
        # .txt 파일 열기
        with open(file, 'r') as f:
            lines = f.readlines()

        # 각 줄을 확인하여 첫 번째 값이 18인 경우 처리
        delete_file = False
        for line in lines:
            # 줄이 비어있지 않고 첫 번째 값이 18이면 삭제 플래그 설정
            if line.strip() and line.split()[0] == '18':
                delete_file = True
                break
        
        # 삭제 플래그가 설정된 경우 .txt 파일과 같은 이름의 .jpg 파일 삭제
        if delete_file:
            # .txt 파일 삭제
            os.remove(file)
            print(f"Deleted: {file}")

            # 동일한 이름의 .jpg 파일 삭제
            jpg_file = file.replace('.txt', '.jpg')
            if os.path.exists(jpg_file):
                os.remove(jpg_file)
                print(f"Deleted: {jpg_file}")
