{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";
  };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};

      lwjglDeps = with pkgs; [
        libGL
        libglvnd
        libxkbcommon
        wayland
        libx11
        libxcursor
        libxi
        libxrandr
        libxext
        stdenv.cc.cc.lib
      ];
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        buildInputs = [
          pkgs.jdk21
          pkgs.gradle
        ] ++ lwjglDeps;

        shellHook = ''
          export LD_LIBRARY_PATH=${pkgs.lib.makeLibraryPath lwjglDeps}:$LD_LIBRARY_PATH
          export NIX_LD_LIBRARY_PATH=${pkgs.lib.makeLibraryPath lwjglDeps}
          export NIX_LD=${pkgs.stdenv.cc.bintools.dynamicLinker}
        '';
      };
    };
}