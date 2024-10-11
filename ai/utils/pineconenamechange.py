import os

def rename_files(directory):
    for filename in os.listdir(directory):
        # Check if the filename does not already start with 'foxtail'
        if not filename.startswith("pinecone"):
            new_filename = "pinecone" + filename
            src = os.path.join(directory, filename)
            dst = os.path.join(directory, new_filename)
            os.rename(src, dst)
            print(f'Renamed: {filename} -> {new_filename}')
        else:
            print(f'Skipped: {filename} (already renamed)')

# Replace 'your_directory_path' with the path to your directory containing the images
directory_path = './'
rename_files(directory_path)