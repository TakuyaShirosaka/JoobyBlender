FROM amazoncorretto:11

RUN yum update -y && \
	yum install -y \
		sudo \
		curl \
		bzip2 \
		tar \
		wget \
		gnupg
		
RUN yum update -y && \
	yum install -y \
		freetype \
		freetype-devel \
		libpng-devel \
		mesa-libGL-devel \
		mesa-libGLU-devel \
		libX11-devel \
		libsm6 \
		libXi \
		libXrender \
		libSM.x86_64 \
		perl-Time-HiRes

#Python
RUN sudo yum install -y cmake qt-devel qt-webkit-devel libxml2-devel libxslt-devel python-devel rpmdevtools gcc gcc-c++ make python-pip
RUN sudo ln -s /usr/bin/qmake-qt4 /usr/bin/qmake
RUN pip install numpy
RUN pip install Pillow

#blender
ENV BLENDER_MAJOR 2.83
ENV BLENDER_VERSION 2.83.10

WORKDIR /
RUN wget https://download.blender.org/release/Blender${BLENDER_MAJOR}/blender-${BLENDER_VERSION}-linux64.tar.xz -O blender.tar.xz
RUN mkdir blender && tar xvf blender.tar.xz -C blender --strip-components 1
RUN chmod 600 /blender
ENV PATH $PATH:/blender
RUN rm blender.tar.xz

#python
COPY ./pythonMacro                /pythonMacro
RUN chmod -R  777 /pythonMacro

#開発用
COPY ./thirdPartyTool/.aws/credentials     /aws/credentials
ENV AWS_CREDENTIAL_PROFILES_FILE           /aws/credentials



