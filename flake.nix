{
  description = "A very basic flake";

  outputs = { self, nixpkgs }:
  let system = "x86_64-linux";
  in {
    # Works, but image loading depends on being in the right path...
    packages.${system} = let pkgs = import nixpkgs { inherit system; }; in {
        autoDrawer = nixpkgs.legacyPackages.${system}.stdenv.mkDerivation rec {
            name = "FTC AutoDrawer";
            pname = "ftc-autodrawer";

            src = pkgs.fetchFromGitHub {
                    owner = "archerd";
                    repo = "FTC-autoDrawer";
                    rev = "master";
                    sha256 = "I+2JLJXmYN28gTpqvqEKcqFD8Zh2ZP9xkUSXHOSoF4s=";
                };

            buildInputs = [ pkgs.jdk ];
            nativeBuildInputs = [ pkgs.ant pkgs.makeWrapper ];

            buildPhase = ''
                cd AnimationTest
                ant default
            '';
            
            installPhase = ''
                mkdir $out
                mkdir $out/bin
                mkdir $out/lib
                cp -dp dist/AnimationTest.jar $out/lib/
                # makeWrapper args feel backwards: first is the executable to be wrapped, then the location to write the wrapper, lastly the arguments for the original executable.
                makeWrapper ${pkgs.jdk}/bin/java $out/bin/${pname} \
                        --add-flags -jar --add-flags $out/lib/AnimationTest.jar
            '';
        };

        default = self.packages.x86_64-linux.autoDrawer;
    };

    devShells.${system}.default = let pkgs = import nixpkgs { inherit system; }; in pkgs.mkShell {
        packages = with pkgs; [ jdk ant ];
    };
  };
}
