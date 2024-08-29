# 개인 과제 _ 인공지능 영상

## pytorch_tensor 주피터노트북

numpy로 행렬 연산을 쉽게 할 수 있다. 파이썬 라이브러리

행렬 생성

행렬 0, 1로 채워서 만들기

랜덤 수로 채운 행렬 생성

랜덤 정수로 채운 행렬 생성

인덱싱

행렬연산(합, 차, 곱, 상수곱, 평균, 전치)

파이토치

텐서에서 리스트나 넘파이어레이로 변경 가능

텐서연산 디바이스 지정

쿠다 또는 씨피유

파이토치에서 텐서(데이터)를 gpu 연산에 사용하려면 

텐서를 gpu에 올려야 한다

파이토치 텐서 다루기

- `torch.unsqueeze()`와 `torch.squeeze()`
- `torch.view()`와 `torch.reshape()`
- `torch.concat()`와 `torch.stack()`

모델 파라미터는 조금씩 변경하며 모델 학습 진행

    오차(loss)를 역전파하며 파라미터 업데이트 값 결정

경사하강법은 오차를 함수로 표현하고 미분하여 그 기울기를 구해 오차의 최솟값 방향으로 파라미터 값 업데이트

파이토치에서는 미분 계산을 자동으로 해준다. 모델의 파라미터를 담고 있는 텐서에 자동 미분을 하도록 설정하면 해당 텐서에 대한 미분값(그래디언트) 저장

## image_classification

Basic Neural Network 구현

데이터셋 내 데이터 시각화 함수 구현

- 이미지 데이터는 0에서 255사이의 픽셀값을 가짐

- 모델이 처리할 수 있는 텐서는 [-1, 1] 범위를 가짐

- 텐서로 변환하여 사용(정규화; data transformation 정의하여 사용)

cifar10으로 테스트

시각화 함수로 데이터 시각화 구현

- 시각화를 위해서 다시 정규화된 텐서에서 복원해야함

- 이후에 화면에 띄우는 작업 `visualize()` 구현

- 데이터셋 및 데이터 로더 불러와서 `visualize()` 함수 테스트 가능

Model 구성

- TwoLayerFC 모델, forward 연산 정의

- Linear Layer로 이미지를 flatten 

- Fully Connected Layer 모듈
  
  - FC1: input data -> hidden feature
  
  - ReLU
  
  - FC2: hidden feature -> output(각 클래스 별 classification 점수)
  
  - nn.Module 을 상속 받은 TwoLayerFC 모델의 _init_ 람수에 모델 레이어를 , forward 함수에 forward 연산을 정의하면 됨

모델 선언, 모델의 레이어 구성 확인, 모델의 파라미터 확인

모델 학습과정 구현

- train() 함수 구현

- 모델의 가중치가 업데이트 되는 과정이 학습과정임

- Optimizer를 초기화 하여 학습(조정) 하려는 모델 파라미터의 그래디언트를 리셋함

- 학습  loop 내에서
  
  - 모델 파라미터의 그래디언트를 리셋 `optimizer.zero_grad()`
  
  - model output 및 loss 계산
  
  - backpropagation `loss.backward()`, 파이토치에서 자동적으로 각 파라미터의 그래디언트를 계산해 저장함
  
  - 계산한 그래디언트를 이용해 파라미터를 업데이트함 `optimizer.step()`

- train() 함수에서 학습 loop 의 내용 구현

train() 함수 구현

- 각 데이터 배치 별 각 스텝 학습

- 초기화, 데이터 불러오기, 모델의 예측값 획득, 정답값과 차이구하기(loss)

- loss 를 backpropagation 하고 네트워크 가중치 업데이트
  
  -          # 1) Optimizer `optimizer`의 gradient 값을 초기화
    
            # 2) 배치 내 데이터를 불러오고 `device`로 데이터가 올라갈 디바이스를 지정함
    
            # 3) Forward pass를 통해 모델의 출력(예측값)을 생성함
    
            # 4) cross entropy loss를 계산함
    
            # 5) Loss를 backpropagation하여 gradient를 전달함
    
            # 6) optimizer로 네트워크 가중치를 업데이트함
    
            # 7) [Optional] 로깅 용으로 loss를 출력함                                                                      

모델 테스트 과정 구현

train모드 말고 eval 모드로 변경

그래디언트 계산이 불필요하므로 막음 : `torch.no_grad()`나 `@torch.no_grad()`

accuracy 계산하여 성능 측정

validation set과 test set에 대하여 측정

    # 1) 배치 내 데이터를 불러오고 `device`로 데이터가 올라갈 디바이스를 지정함

    # 2) Forward pass를 통해 모델의 출력(예측값)을 생성함

    # 3) `mode`가 validation(`val`)일 때는 loss 값을 계산함

    # 4) 예측한 클래스를 추출함(top-1). 예측값이 각 클래스의 점수이므로, 값이 최대인 클래스를 찾음

    # 5) 정답을 맞춘 개수를 누적합함

    # 6) Accurcy를 계산함

모델 학습 진행

- 학습 설정값 불러오고

- 데이터셋 및 데이터 로더 불러오고

- 종합하여 실행

- 1) 실행 디바이스 및 configuration 설정
  2) 이미지 데이터의 transform 정의하기
  3) 데이터로더 불러오기
  4) 모델을 선언하기
  5) 모델을 GPU에 올리기 (연산을 GPU로 하기 위함)
  6) Optimizer 선언하기: SGD 사용
  7) epoch만큼 반복하며 학습-평가를 수행하기: 한 epoch에서 학습 - 평가 순으로 수행하기

CNN 구현

- ThreeLayerConvNet 모델 구성 및 forward 연산 정의

- Convolution layer 5x5 필터, 2픽셀의 zero padding 사용

- ReLU 활성화함수

- Convolution layer 3x3 필터, 1픽셀의 zero padding 사용

- ReLU 활성화 함수

- Fully connent layer: 각 클래스 별 classification  점수로 매핑할 레이어

-         # 1) in_channel -> channel_1으로 가는 첫 번째 convolution layer 정의
  
          # 2) channel_1 -> channel_2으로 가는 두 번째 convolution layer 정의
  
          # 3) ReLU 활성화 함수 정의
  
          # 4) hidden feature를 클래스 별 분류 점수로 바꿀 FC layer 정의




























































