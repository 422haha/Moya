import os
import xml.etree.ElementTree as ET

# Pascal VOC 형식의 XML 파일이 있는 디렉토리
xml_dir = './'  # XML 파일들이 저장된 경로
# YOLO 형식의 텍스트 파일이 저장될 디렉토리
yolo_dir = './labels'  # YOLO 형식의 .txt 파일을 저장할 경로

# 디렉토리가 없으면 생성
os.makedirs(yolo_dir, exist_ok=True)

# XML 파일에서 YOLO 형식으로 변환하는 함수
def convert_xml_to_yolo(xml_file, yolo_file):
    tree = ET.parse(xml_file)
    root = tree.getroot()

    # 이미지 크기 정보
    size = root.find('size')
    img_width = int(size.find('width').text)
    img_height = int(size.find('height').text)

    # YOLO 형식으로 쓸 내용을 저장할 리스트
    yolo_data = []

    # XML의 <object> 태그를 순회하며 바운딩 박스를 YOLO 형식으로 변환
    for obj in root.findall('object'):
        class_id = int(obj.find('name').text)  # <name> 값은 클래스 번호로 처리
        bndbox = obj.find('bndbox')
        xmin = int(bndbox.find('xmin').text)
        ymin = int(bndbox.find('ymin').text)
        xmax = int(bndbox.find('xmax').text)
        ymax = int(bndbox.find('ymax').text)

        # 바운딩 박스 중심 좌표 및 폭, 높이 계산 (정규화)
        x_center = (xmin + xmax) / 2.0 / img_width
        y_center = (ymin + ymax) / 2.0 / img_height
        width = (xmax - xmin) / img_width
        height = (ymax - ymin) / img_height

        # YOLO 형식: <클래스> <x_center> <y_center> <width> <height>
        yolo_data.append(f"{class_id} {x_center:.6f} {y_center:.6f} {width:.6f} {height:.6f}")

    # YOLO 형식의 내용을 텍스트 파일로 저장
    with open(yolo_file, 'w') as f:
        if yolo_data:
            f.write("\n".join(yolo_data))


# XML 파일을 순회하며 변환
for xml_file in os.listdir(xml_dir):
    if xml_file.endswith('.xml'):
        # 각 XML 파일에 대한 YOLO 텍스트 파일 경로 설정
        yolo_file = os.path.join(yolo_dir, xml_file.replace('.xml', '.txt'))
        convert_xml_to_yolo(os.path.join(xml_dir, xml_file), yolo_file)

print("XML -> YOLO 변환 완료!")
